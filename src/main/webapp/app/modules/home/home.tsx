import './home.scss';

import React, { useState } from 'react';
import { Translate } from 'react-jhipster';
import { Alert, Col, Row, Container, Collapse } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import { JobRequest } from 'app/entities/job-request/job-request';
import { JobExecutionReport } from 'app/entities/job-execution-report/job-execution-report';
import { JobReport } from 'app/entities/job-report/job-report';
import { SanitizationReport } from 'app/entities/sanitization-report/sanitization-report';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  const [openSections, setOpenSections] = useState<Set<string>>(new Set());

  const toggle = (section: string) =>
    setOpenSections(prev => {
      const next = new Set(prev);
      if (next.has(section)) {
        next.delete(section);
      } else {
        next.add(section);
      }
      return next;
    });

  return (
    <div className="home-page">
      {/* Header Section */}
      {!account.login && ( // So the home page doesn't show up when signed in
        <>
          <header className="home-header">
            <Container>
              <Row className="align-items-center">
                <Col md="6">
                  <h1 className="header-title">
                    <Translate contentKey="home.title">Welcome To Cyber AI Group!</Translate>
                  </h1>
                  <p className="header-subtitle">
                    <Translate contentKey="home.subtitle">Where Cyber AI Meets Innovation</Translate>
                  </p>
                </Col>
              </Row>
            </Container>
          </header>

          {/* Mission Statement Section */}
          <section className="mission-section">
            <Container>
              <div className="mission-grad">
                <div className="mission-card">
                  <h2 className="mission-title">
                    <Translate contentKey="home.mission-title">Our Mission</Translate>
                  </h2>
                  <p className="mission-statement">
                    <Translate contentKey="home.mission-statement">
                      To empower cybersecurity professionals and organizations through cutting-edge AI technologies, innovative solutions,
                      and comprehensive training. We are dedicated to advancing the cybersecurity landscape by combining artificial
                      intelligence with expert knowledge to create safer digital environments.
                    </Translate>
                  </p>
                </div>
              </div>
            </Container>
          </section>

          {/* AI Section */}
          <section className="AI-section">
            <Container>
              <div className="AI-card">
                <h2 className="AI-title">
                  <Translate contentKey="home.artificial-intelligence">Artificial Intelligence</Translate>
                </h2>
                <p className="AI-description">
                  <Translate contentKey="home.AI-description">
                    Artificial Intelligence (AI) is revolutionizing the cybersecurity landscape by providing advanced tools and techniques
                    to detect, prevent, and respond to cyber threats. AI-powered systems can analyze vast amounts of data in real-time,
                    identify patterns, and predict potential attacks, enabling organizations to stay one step ahead of threat actors. At
                    Cyber AI Group, we leverage the power of AI to enhance cybersecurity defenses and protect digital assets. Our AI
                    Cybersecurity solutions are designed to provide comprehensive protection against the evolving threat landscape and
                    capitalize on AI initiatives to enhance cybersecurity capabilities.
                  </Translate>
                </p>
              </div>
            </Container>
          </section>

          {/* Cybersecurity Section */}
          <section className="cyber-section">
            <Container>
              <div className="cyber-card">
                <h2 className="cyber-title">
                  <Translate contentKey="home.cyber">Cybersecurity</Translate>
                </h2>
                <p className="cyber-description">
                  <Translate contentKey="home.cyber-description">
                    Cybersecurity threats are becoming increasingly sophisticated, requiring organizations to adopt proactive measures to
                    protect their digital assets. At Cyber AI Group, we are committed to providing cutting-edge cybersecurity solutions that
                    leverage the power of artificial intelligence. Our AI-driven cybersecurity tools are designed to detect and respond to
                    threats in real-time, helping organizations stay ahead of cybercriminals, safeguard their sensitive information, and
                    prioritize their security posture to support business operations. The shifting sophistication of cyber threats has the
                    potential to significantly disrupt an organization&apos;s operations. Cyber AI group is dedicated to providing the
                    necessary tools to ensure business continuity, disaster recovery, and risk management to mitigate vulnerabilities and
                    provide a defense-in-depth strategy to prevent a loss of confidentiality, integrity, and availability of information
                    assets.
                  </Translate>
                </p>
              </div>
            </Container>
          </section>
        </>
      )}

      {/* Main Content Section */}
      <section className="main-content">
        <Container>
          {account?.login && (
            <div>
              <Alert color="success">
                <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                  You are logged in as user {account.login}.
                </Translate>
              </Alert>

              {[
                { key: 'job-request', label: 'Job Requests', Component: JobRequest },
                { key: 'sanitization-report', label: 'Sanitization Reports', Component: SanitizationReport },
                { key: 'job-execution-report', label: 'Job Execution Reports', Component: JobExecutionReport },
                { key: 'job-report', label: 'Job Reports', Component: JobReport },
              ].map(({ key, label, Component }) => (
                <div key={key} className="entity-section">
                  <button className="entity-section-toggle" onClick={() => toggle(key)}>
                    <span>{label}</span>
                    <span className={`entity-section-arrow ${openSections.has(key) ? 'open' : ''}`}>&#9660;</span>
                  </button>
                  <Collapse isOpen={openSections.has(key)}>
                    <div className="entity-section-body">
                      <Component />
                    </div>
                  </Collapse>
                </div>
              ))}
            </div>
          )}
          <div className="questions-section">
            <h3>
              <Translate contentKey="home.question">If you have any question on Cyber AI Group:</Translate>
            </h3>

            <ul>
              <li>
                <a href="https://www.indianatech.edu" target="_blank" rel="noopener noreferrer">
                  <Translate contentKey="home.link.homepage">AICyber homepage</Translate>
                </a>
              </li>
              <li>
                <a href="https://twitter.com/AICyber" target="_blank" rel="noopener noreferrer">
                  <Translate contentKey="home.link.follow">Follow @AICyber on Twitter</Translate>
                </a>
              </li>
            </ul>
          </div>
        </Container>
      </section>
    </div>
  );
};

export default Home;
