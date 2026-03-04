import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './job-report.reducer';

export const JobReportDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const jobReportEntity = useAppSelector(state => state.jobReport.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="jobReportDetailsHeading">
          <Translate contentKey="cyberClinicApp.jobReport.detail.title">JobReport</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{jobReportEntity.id}</dd>
          <dt>
            <span id="reportDate">
              <Translate contentKey="cyberClinicApp.jobReport.reportDate">Report Date</Translate>
            </span>
          </dt>
          <dd>
            {jobReportEntity.reportDate ? <TextFormat value={jobReportEntity.reportDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="details">
              <Translate contentKey="cyberClinicApp.jobReport.details">Details</Translate>
            </span>
          </dt>
          <dd>{jobReportEntity.details}</dd>
          <dt>
            <span id="isSuccessful">
              <Translate contentKey="cyberClinicApp.jobReport.isSuccessful">Is Successful</Translate>
            </span>
          </dt>
          <dd>{jobReportEntity.isSuccessful ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="cyberClinicApp.jobReport.jobRequest">Job Request</Translate>
          </dt>
          <dd>{jobReportEntity.jobRequest ? jobReportEntity.jobRequest.id : ''}</dd>
          <dt>
            <Translate contentKey="cyberClinicApp.jobReport.user">User</Translate>
          </dt>
          <dd>{jobReportEntity.user ? jobReportEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/job-report" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/job-report/${jobReportEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default JobReportDetail;
