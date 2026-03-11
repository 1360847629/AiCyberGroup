import './footer.scss';

import React from 'react';
import { Translate } from 'react-jhipster';
import { Col, Row, Container } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHome, faCode, faLink } from '@fortawesome/free-solid-svg-icons';

const Footer = () => (
  <footer className="app-footer">
    <Container>
      <Row className="footer-content">
        <Col md="4" className="footer-info">
          <h5>
            <Translate contentKey="footer.about">About Cyber AI Group</Translate>
          </h5>
          <p>
            <Translate contentKey="footer.description">
              Leading the future of cybersecurity through artificial intelligence and innovative solutions.
            </Translate>
          </p>
        </Col>
        <Col md="4" className="footer-social">
          <h5>
            <Translate contentKey="footer.follow">Follow Us</Translate>
          </h5>
          <div className="social-icons">
            <a href="https://www.indianatech.edu" target="_blank" rel="noopener noreferrer" className="social-icon" title="Homepage">
              <FontAwesomeIcon icon={faHome} />
            </a>
            <a
              href="https://github.com/davidlislc/AiCyberGroup"
              target="_blank"
              rel="noopener noreferrer"
              className="social-icon"
              title="GitHub"
            >
              <FontAwesomeIcon icon={faCode} />
            </a>
            <a href="https://twitter.com/AICyber" target="_blank" rel="noopener noreferrer" className="social-icon" title="Twitter">
              <FontAwesomeIcon icon={faLink} />
            </a>
          </div>
        </Col>
        <Col md="4" className="footer-sponsors">
          <a href="https://lillyendowment.org/" target="_blank" rel="noopener noreferrer" className="sponsor-link">
            <img src="/content/images/lily_logo.jpg" alt="Sponsor Logo" className="sponsor-logo" />
          </a>
        </Col>
      </Row>
      <Row className="footer-bottom">
        <Col md="12" className="text-center">
          <p>
            <Translate contentKey="footer.copyright">© 2026 Cyber AI Group. All rights reserved.</Translate>
          </p>
        </Col>
      </Row>
    </Container>
  </footer>
);

export default Footer;
