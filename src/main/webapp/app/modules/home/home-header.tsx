import React from 'react';
import { Translate } from 'react-jhipster';
import { Col, Container, Row } from 'reactstrap';

const HomeHeader = () => (
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
        <Col md="3" className="header-sponsor-inline">
          <a href="https://lillyendowment.org/" target="_blank" rel="noopener noreferrer" className="header-sponsor-link">
            <img src="/content/images/lily_logo.jpg" alt="Sponsor Logo" className="header-sponsor-logo" />
          </a>
        </Col>
      </Row>
    </Container>
  </header>
);

export default HomeHeader;
