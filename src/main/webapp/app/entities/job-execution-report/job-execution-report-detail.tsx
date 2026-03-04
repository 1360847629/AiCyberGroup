import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './job-execution-report.reducer';

export const JobExecutionReportDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const jobExecutionReportEntity = useAppSelector(state => state.jobExecutionReport.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="jobExecutionReportDetailsHeading">
          <Translate contentKey="cyberClinicApp.jobExecutionReport.detail.title">JobExecutionReport</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{jobExecutionReportEntity.id}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="cyberClinicApp.jobExecutionReport.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>
            {jobExecutionReportEntity.startTime ? (
              <TextFormat value={jobExecutionReportEntity.startTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="cyberClinicApp.jobExecutionReport.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>
            {jobExecutionReportEntity.endTime ? (
              <TextFormat value={jobExecutionReportEntity.endTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="executionNode">
              <Translate contentKey="cyberClinicApp.jobExecutionReport.executionNode">Execution Node</Translate>
            </span>
          </dt>
          <dd>{jobExecutionReportEntity.executionNode}</dd>
          <dt>
            <span id="executionLog">
              <Translate contentKey="cyberClinicApp.jobExecutionReport.executionLog">Execution Log</Translate>
            </span>
          </dt>
          <dd>{jobExecutionReportEntity.executionLog}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="cyberClinicApp.jobExecutionReport.status">Status</Translate>
            </span>
          </dt>
          <dd>{jobExecutionReportEntity.status}</dd>
          <dt>
            <Translate contentKey="cyberClinicApp.jobExecutionReport.jobRequest">Job Request</Translate>
          </dt>
          <dd>{jobExecutionReportEntity.jobRequest ? jobExecutionReportEntity.jobRequest.id : ''}</dd>
          <dt>
            <Translate contentKey="cyberClinicApp.jobExecutionReport.user">User</Translate>
          </dt>
          <dd>{jobExecutionReportEntity.user ? jobExecutionReportEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/job-execution-report" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/job-execution-report/${jobExecutionReportEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default JobExecutionReportDetail;
