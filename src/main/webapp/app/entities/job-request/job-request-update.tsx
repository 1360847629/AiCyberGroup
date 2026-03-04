import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedBlobField, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { Status } from 'app/shared/model/enumerations/status.model';
import { RequestType } from 'app/shared/model/enumerations/request-type.model';
import { createEntity, getEntity, reset, updateEntity } from './job-request.reducer';

export const JobRequestUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const jobRequestEntity = useAppSelector(state => state.jobRequest.entity);
  const loading = useAppSelector(state => state.jobRequest.loading);
  const updating = useAppSelector(state => state.jobRequest.updating);
  const updateSuccess = useAppSelector(state => state.jobRequest.updateSuccess);
  const statusValues = Object.keys(Status);
  const requestTypeValues = Object.keys(RequestType);

  const handleClose = () => {
    navigate(`/job-request${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    if (values.score !== undefined && typeof values.score !== 'number') {
      values.score = Number(values.score);
    }

    const entity = {
      ...jobRequestEntity,
      ...values,
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
      ? {}
      : {
          status: 'PENDING',
          requestType: 'BATCH',
          ...jobRequestEntity,
          user: jobRequestEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cyberClinicApp.jobRequest.home.createOrEditLabel" data-cy="JobRequestCreateUpdateHeading">
            <Translate contentKey="cyberClinicApp.jobRequest.home.createOrEditLabel">Create or edit a JobRequest</Translate>
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
                  id="job-request-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedBlobField
                label={translate('cyberClinicApp.jobRequest.fileContent')}
                id="job-request-fileContent"
                name="fileContent"
                data-cy="fileContent"
                openActionLabel={translate('entity.action.open')}
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('cyberClinicApp.jobRequest.score')}
                id="job-request-score"
                name="score"
                data-cy="score"
                type="text"
              />
              <ValidatedField
                label={translate('cyberClinicApp.jobRequest.status')}
                id="job-request-status"
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
                label={translate('cyberClinicApp.jobRequest.requestType')}
                id="job-request-requestType"
                name="requestType"
                data-cy="requestType"
                type="select"
              >
                {requestTypeValues.map(requestType => (
                  <option value={requestType} key={requestType}>
                    {translate(`cyberClinicApp.RequestType.${requestType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="job-request-user"
                name="user"
                data-cy="user"
                label={translate('cyberClinicApp.jobRequest.user')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/job-request" replace color="info">
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

export default JobRequestUpdate;
