import './footer.scss';

import React from 'react';
import { Translate } from 'react-jhipster';

const bottomLinks = [
  { href: 'https://indianatech.edu', label: 'Indiana Tech', icon: '/content/images/it_logo.svg' },
  { href: 'https://techcyberwarriors.org', label: 'Cyber Warriors', icon: '/content/images/shield.svg' },
  { href: 'https://lillyendowment.org', label: 'Lilly Endowment', icon: '/content/images/lilly.svg' },
];

const Footer = () => (
  <footer className="app-footer">
    <div className="footer-partners">
      {bottomLinks.map(({ href, label, icon }) => (
        <a key={label} href={href} target="_blank" rel="noopener noreferrer" className="partner-link">
          <img src={icon} alt="" className="partner-icon" /> {label}
        </a>
      ))}
    </div>
    <div className="footer-bottom">
      <div className="footer-brand">
        <img src="/content/images/it_logo.svg" alt="Logo" className="footer-brand-logo" />
        <span>CyberAI</span>
      </div>
      <span className="footer-copyright">
        <Translate contentKey="footer.copyright">&copy; 2026 Cyber AI Group. All Rights Reserved</Translate>
      </span>
      <a href="https://lillyendowment.org" target="_blank" rel="noopener noreferrer" className="footer-sponsor-link">
        <img src="/content/images/lily_logo.jpg" alt="Lilly Endowment" className="footer-sponsor-logo" />
      </a>
    </div>
  </footer>
);

export default Footer;
