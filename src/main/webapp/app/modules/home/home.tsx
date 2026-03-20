import './home.scss';

import React from 'react';
import { Translate } from 'react-jhipster';
import { Alert, Col, Row, Container } from 'reactstrap';
import HomeHeader from './home-header';
import AIHeader from './ai-header';
import CyberHeader from './cyber-header';
import MissionHeader from './mission-header';
import { useAppSelector } from 'app/config/store';
import { JobRequest } from 'app/entities/job-request/job-request';
import { JobExecutionReport } from 'app/entities/job-execution-report/job-execution-report';
import { JobReport } from 'app/entities/job-report/job-report';
import { SanitizationReport } from 'app/entities/sanitization-report/sanitization-report';
export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <div className="home-page">
      {!account?.login && (
        <>
          {/* Header Section */}
          <HomeHeader />

          {/* Mission Statement Section */}

          <MissionHeader />

          {/* AI Section */}

          <AIHeader />
          {/* Cybersecurity Section */}
          <CyberHeader />
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

              <JobRequest />
              <SanitizationReport />
              <JobExecutionReport />
              <JobReport />
            </div>
          )}
        </Container>
      </section>
    </div>
  );
};

export default Home;
