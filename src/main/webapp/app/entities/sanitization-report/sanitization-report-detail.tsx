import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sanitization-report.reducer';

export const SanitizationReportDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const sanitizationReportEntity = useAppSelector(state => state.sanitizationReport.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sanitizationReportDetailsHeading">
          <Translate contentKey="cyberClinicApp.sanitizationReport.detail.title">SanitizationReport</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{sanitizationReportEntity.id}</dd>
          <dt>
            <span id="reportDate">
              <Translate contentKey="cyberClinicApp.sanitizationReport.reportDate">Report Date</Translate>
            </span>
          </dt>
          <dd>
            {sanitizationReportEntity.reportDate ? (
              <TextFormat value={sanitizationReportEntity.reportDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="details">
              <Translate contentKey="cyberClinicApp.sanitizationReport.details">Details</Translate>
            </span>
          </dt>
          <dd>{sanitizationReportEntity.details}</dd>
          <dt>
            <span id="isSuccessful">
              <Translate contentKey="cyberClinicApp.sanitizationReport.isSuccessful">Is Successful</Translate>
            </span>
          </dt>
          <dd>{sanitizationReportEntity.isSuccessful ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="cyberClinicApp.sanitizationReport.jobRequest">Job Request</Translate>
          </dt>
          <dd>{sanitizationReportEntity.jobRequest ? sanitizationReportEntity.jobRequest.id : ''}</dd>
          <dt>
            <Translate contentKey="cyberClinicApp.sanitizationReport.user">User</Translate>
          </dt>
          <dd>{sanitizationReportEntity.user ? sanitizationReportEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/sanitization-report" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sanitization-report/${sanitizationReportEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SanitizationReportDetail;
