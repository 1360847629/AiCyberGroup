import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, byteSize, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './job-request.reducer';

export const JobRequestDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const jobRequestEntity = useAppSelector(state => state.jobRequest.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="jobRequestDetailsHeading">
          <Translate contentKey="cyberClinicApp.jobRequest.detail.title">JobRequest</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{jobRequestEntity.id}</dd>
          <dt>
            <span id="fileContent">
              <Translate contentKey="cyberClinicApp.jobRequest.fileContent">File Content</Translate>
            </span>
          </dt>
          <dd>
            {jobRequestEntity.fileContent ? (
              <div>
                {jobRequestEntity.fileContentContentType ? (
                  <a onClick={openFile(jobRequestEntity.fileContentContentType, jobRequestEntity.fileContent)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {jobRequestEntity.fileContentContentType}, {byteSize(jobRequestEntity.fileContent)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="score">
              <Translate contentKey="cyberClinicApp.jobRequest.score">Score</Translate>
            </span>
          </dt>
          <dd>{jobRequestEntity.score}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="cyberClinicApp.jobRequest.status">Status</Translate>
            </span>
          </dt>
          <dd>{jobRequestEntity.status}</dd>
          <dt>
            <span id="fileType">
              <Translate contentKey="cyberClinicApp.jobRequest.fileType">File Type</Translate>
            </span>
          </dt>
          <dd>{jobRequestEntity.fileType}</dd>
          <dt>
            <span id="requestType">
              <Translate contentKey="cyberClinicApp.jobRequest.requestType">Request Type</Translate>
            </span>
          </dt>
          <dd>{jobRequestEntity.requestType}</dd>
          <dt>
            <span id="priority">
              <Translate contentKey="cyberClinicApp.jobRequest.priority">Priority</Translate>
            </span>
          </dt>
          <dd>{jobRequestEntity.priority}</dd>
          <dt>
            <Translate contentKey="cyberClinicApp.jobRequest.user">User</Translate>
          </dt>
          <dd>{jobRequestEntity.user ? jobRequestEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/job-request" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/job-request/${jobRequestEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default JobRequestDetail;
