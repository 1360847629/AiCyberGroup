import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getJobRequests } from 'app/entities/job-request/job-request.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { Status } from 'app/shared/model/enumerations/status.model';
import { createEntity, getEntity, reset, updateEntity } from './job-execution-report.reducer';

export const JobExecutionReportUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const jobRequests = useAppSelector(state => state.jobRequest.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const jobExecutionReportEntity = useAppSelector(state => state.jobExecutionReport.entity);
  const loading = useAppSelector(state => state.jobExecutionReport.loading);
  const updating = useAppSelector(state => state.jobExecutionReport.updating);
  const updateSuccess = useAppSelector(state => state.jobExecutionReport.updateSuccess);
  const statusValues = Object.keys(Status);

  const handleClose = () => {
    navigate(`/job-execution-report${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getJobRequests({}));
    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.startTime = convertDateTimeToServer(values.startTime);
    values.endTime = convertDateTimeToServer(values.endTime);

    const entity = {
      ...jobExecutionReportEntity,
      ...values,
      jobRequest: jobRequests.find(it => it.id.toString() === values.jobRequest?.toString()),
      user: users.find(it => it.id.toString() === values.user?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          startTime: displayDefaultDateTime(),
          endTime: displayDefaultDateTime(),
        }
      : {
          status: 'PENDING',
          ...jobExecutionReportEntity,
          startTime: convertDateTimeFromServer(jobExecutionReportEntity.startTime),
          endTime: convertDateTimeFromServer(jobExecutionReportEntity.endTime),
          jobRequest: jobExecutionReportEntity?.jobRequest?.id,
          user: jobExecutionReportEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cyberClinicApp.jobExecutionReport.home.createOrEditLabel" data-cy="JobExecutionReportCreateUpdateHeading">
            <Translate contentKey="cyberClinicApp.jobExecutionReport.home.createOrEditLabel">Create or edit a JobExecutionReport</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="job-execution-report-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cyberClinicApp.jobExecutionReport.startTime')}
                id="job-execution-report-startTime"
                name="startTime"
                data-cy="startTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('cyberClinicApp.jobExecutionReport.endTime')}
                id="job-execution-report-endTime"
                name="endTime"
                data-cy="endTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('cyberClinicApp.jobExecutionReport.executionNode')}
                id="job-execution-report-executionNode"
                name="executionNode"
                data-cy="executionNode"
                type="text"
              />
              <ValidatedField
                label={translate('cyberClinicApp.jobExecutionReport.executionLog')}
                id="job-execution-report-executionLog"
                name="executionLog"
                data-cy="executionLog"
                type="textarea"
              />
              <ValidatedField
                label={translate('cyberClinicApp.jobExecutionReport.status')}
                id="job-execution-report-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {statusValues.map(status => (
                  <option value={status} key={status}>
                    {translate(`cyberClinicApp.Status.${status}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="job-execution-report-jobRequest"
                name="jobRequest"
                data-cy="jobRequest"
                label={translate('cyberClinicApp.jobExecutionReport.jobRequest')}
                type="select"
              >
                <option value="" key="0" />
                {jobRequests
                  ? jobRequests.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="job-execution-report-user"
                name="user"
                data-cy="user"
                label={translate('cyberClinicApp.jobExecutionReport.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/job-execution-report" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default JobExecutionReportUpdate;
