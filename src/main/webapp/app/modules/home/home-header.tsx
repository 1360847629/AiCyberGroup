import React from 'react';
import { Translate } from 'react-jhipster';

const HomeHeader = () => (
  <header className="landing-banner" id="landing-banner">
    <div>
      <h1 className="banner-title">
        <Translate contentKey="home.title">CyberAI</Translate>
      </h1>
      <a href="https://lillyendowment.org/" target="_blank" rel="noopener noreferrer" className="banner-sponsor">
        <img src="/content/images/lily_logo.jpg" alt="Lilly Endowment" />
      </a>
    </div>
    <div className="banner-right">
      <p className="banner-description">
        <Translate contentKey="home.subtitle">
          AI-powered cybersecurity tools designed to protect, detect, and respond to emerging threats in real time.
        </Translate>
      </p>
      <div className="banner-divider" />
    </div>
  </header>
);

export default HomeHeader;
