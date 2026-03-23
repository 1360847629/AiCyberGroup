import './home.scss';

import React, { useEffect, useRef } from 'react';
import { Translate } from 'react-jhipster';
import { Alert, Container } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import HomeHeader from './home-header';
import MissionHeader from './mission-header';
import { useAppSelector } from 'app/config/store';
import { JobRequest } from 'app/entities/job-request/job-request';
import { JobExecutionReport } from 'app/entities/job-execution-report/job-execution-report';
import { JobReport } from 'app/entities/job-report/job-report';
import { SanitizationReport } from 'app/entities/sanitization-report/sanitization-report';

const serviceTopics = [
  {
    title: 'Artificial Intelligence',
    description:
      'At Cyber AI Group, we leverage the power of AI to enhance cybersecurity defenses and protect digital assets. Our AI Cybersecurity solutions are designed to provide comprehensive protection against the evolving threat landscape and capitalize on AI initiatives to enhance cybersecurity capabilities.',
  },
  {
    title: 'Cybersecurity',
    description:
      'Cyber AI group is dedicated to providing the necessary tools to ensure business continuity, disaster recovery, and risk management to mitigate vulnerabilities and provide a defense-in-depth strategy to prevent a loss of confidentiality, integrity, and availability of information assets.',
  },
  {
    title: 'About Cyber AI Group',
    description: 'Leading the future of cybersecurity through artificial intelligence and innovative solutions.',
  },
];

const TopicCard = ({ title, description }: { title: string; description: string }) => {
  const cardRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const animationDelay = Math.random() * 600;
    const observer = new IntersectionObserver(
      entries => {
        entries.forEach(entry => {
          if (entry.isIntersecting) {
            setTimeout(() => entry.target.classList.add('fade-in'), animationDelay);
            observer.unobserve(entry.target);
          }
        });
      },
      { threshold: 0.15 },
    );
    if (cardRef.current) observer.observe(cardRef.current);
    return () => observer.disconnect();
  }, []);

  return (
    <div ref={cardRef} className="topic-card">
      <h3 className="topic-card-title">{title}</h3>
      <p className="topic-card-description">{description}</p>
      <div className="topic-card-footer">
        Read more
        <span className="chevron-icon">
          <FontAwesomeIcon icon="chevron-right" size="xs" />
        </span>
      </div>
    </div>
  );
};

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <div className="home-page">
      {!account?.login && (
        <>
          <HomeHeader />
          <MissionHeader />

          <section className="topics-section">
            <h2 className="topics-label">What are we about</h2>
            <div className="topics-grid">
              {serviceTopics.map((topic, index) => (
                <TopicCard key={index} title={topic.title} description={topic.description} />
              ))}
            </div>
          </section>
        </>
      )}

      {account?.login && (
        <section className="main-content">
          <Container>
            <Alert color="success">
              <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                You are logged in as user {account.login}.
              </Translate>
            </Alert>
            <JobRequest />
            <SanitizationReport />
            <JobExecutionReport />
            <JobReport />
          </Container>
        </section>
      )}
    </div>
  );
};

export default Home;
