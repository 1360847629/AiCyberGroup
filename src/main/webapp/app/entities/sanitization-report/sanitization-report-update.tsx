import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getJobRequests } from 'app/entities/job-request/job-request.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { createEntity, getEntity, reset, updateEntity } from './sanitization-report.reducer';

export const SanitizationReportUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const jobRequests = useAppSelector(state => state.jobRequest.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const sanitizationReportEntity = useAppSelector(state => state.sanitizationReport.entity);
  const loading = useAppSelector(state => state.sanitizationReport.loading);
  const updating = useAppSelector(state => state.sanitizationReport.updating);
  const updateSuccess = useAppSelector(state => state.sanitizationReport.updateSuccess);

  const handleClose = () => {
    navigate(`/sanitization-report${location.search}`);
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
    values.reportDate = convertDateTimeToServer(values.reportDate);

    const entity = {
      ...sanitizationReportEntity,
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
          reportDate: displayDefaultDateTime(),
        }
      : {
          ...sanitizationReportEntity,
          reportDate: convertDateTimeFromServer(sanitizationReportEntity.reportDate),
          jobRequest: sanitizationReportEntity?.jobRequest?.id,
          user: sanitizationReportEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cyberClinicApp.sanitizationReport.home.createOrEditLabel" data-cy="SanitizationReportCreateUpdateHeading">
            <Translate contentKey="cyberClinicApp.sanitizationReport.home.createOrEditLabel">Create or edit a SanitizationReport</Translate>
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
                  id="sanitization-report-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cyberClinicApp.sanitizationReport.reportDate')}
                id="sanitization-report-reportDate"
                name="reportDate"
                data-cy="reportDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('cyberClinicApp.sanitizationReport.details')}
                id="sanitization-report-details"
                name="details"
                data-cy="details"
                type="textarea"
              />
              <ValidatedField
                label={translate('cyberClinicApp.sanitizationReport.isSuccessful')}
                id="sanitization-report-isSuccessful"
                name="isSuccessful"
                data-cy="isSuccessful"
                check
                type="checkbox"
              />
              <ValidatedField
                id="sanitization-report-jobRequest"
                name="jobRequest"
                data-cy="jobRequest"
                label={translate('cyberClinicApp.sanitizationReport.jobRequest')}
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
                id="sanitization-report-user"
                name="user"
                data-cy="user"
                label={translate('cyberClinicApp.sanitizationReport.user')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/sanitization-report" replace color="info">
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

export default SanitizationReportUpdate;
