import React from 'react';
import { Translate } from 'react-jhipster';
import { Container } from 'reactstrap';

const CyberHeader = () => (
  <section className="cyber-section">
    <Container>
      <div className="cyber-card">
        <h2 className="cyber-title">
          <Translate contentKey="home.cyber">Cybersecurity</Translate>
        </h2>
        <p className="cyber-description">
          <Translate contentKey="home.cyber-description">
            Cybersecurity threats are becoming increasingly sophisticated, requiring organizations to adopt proactive measures to protect
            their digital assets. At Cyber AI Group, we are committed to providing cutting-edge cybersecurity solutions that leverage the
            power of artificial intelligence. Our AI-driven cybersecurity tools are designed to detect and respond to threats in real-time,
            helping organizations stay ahead of cybercriminals, safeguard their sensitive information, and prioritize their security posture
            to support business operations. The shifting sophistication of cyber threats has the potential to significantly disrupt an
            organization&apos;s operations. Cyber AI group is dedicated to providing the necessary tools to ensure business continuity,
            disaster recovery, and risk management to mitigate vulnerabilities and provide a defense-in-depth strategy to prevent a loss of
            confidentiality, integrity, and availability of information assets.
          </Translate>
        </p>
      </div>
    </Container>
  </section>
);

export default CyberHeader;
