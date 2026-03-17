import React from 'react';
import { Translate } from 'react-jhipster';
import { Container } from 'reactstrap';

const MissionHeader = () => (
  <section className="mission-section">
    <Container>
      <div className="mission-grad">
        <div className="mission-card">
          <h2 className="mission-title">
            <Translate contentKey="home.mission-title">Our Mission</Translate>
          </h2>
          <p className="mission-statement">
            <Translate contentKey="home.mission-statement">
              To empower cybersecurity professionals and organizations through cutting-edge AI technologies, innovative solutions, and
              comprehensive training. We are dedicated to advancing the cybersecurity landscape by combining artificial intelligence with
              expert knowledge to create safer digital environments.
            </Translate>
          </p>
        </div>
      </div>
    </Container>
  </section>
);

export default MissionHeader;
