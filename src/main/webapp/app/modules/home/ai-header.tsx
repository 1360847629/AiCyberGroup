import React from 'react';
import { Translate } from 'react-jhipster';
import { Container } from 'reactstrap';

const AIHeader = () => (
  <section className="AI-section">
    <Container>
      <div className="AI-card">
        <h2 className="AI-title">
          <Translate contentKey="home.artificial-intelligence">Artificial Intelligence</Translate>
        </h2>
        <p className="AI-description">
          <Translate contentKey="home.AI-description">
            Artificial Intelligence (AI) is revolutionizing the cybersecurity landscape by providing advanced tools and techniques to
            detect, prevent, and respond to cyber threats. AI-powered systems can analyze vast amounts of data in real-time, identify
            patterns, and predict potential attacks, enabling organizations to stay one step ahead of threat actors. At Cyber AI Group, we
            leverage the power of AI to enhance cybersecurity defenses and protect digital assets. Our AI Cybersecurity soltuions are
            designed to provide comprehensive protection against the evolving threat landscape and capitalize on AI initiatives to enhance
            cybersecurity capabiltiies.
          </Translate>
        </p>
      </div>
    </Container>
  </section>
);

export default AIHeader;
