import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getJobRequests } from 'app/entities/job-request/job-request.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { createEntity, getEntity, reset, updateEntity } from './job-report.reducer';

export const JobReportUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const jobRequests = useAppSelector(state => state.jobRequest.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const jobReportEntity = useAppSelector(state => state.jobReport.entity);
  const loading = useAppSelector(state => state.jobReport.loading);
  const updating = useAppSelector(state => state.jobReport.updating);
  const updateSuccess = useAppSelector(state => state.jobReport.updateSuccess);

  const handleClose = () => {
    navigate(`/job-report${location.search}`);
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
      ...jobReportEntity,
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
          ...jobReportEntity,
          reportDate: convertDateTimeFromServer(jobReportEntity.reportDate),
          jobRequest: jobReportEntity?.jobRequest?.id,
          user: jobReportEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cyberClinicApp.jobReport.home.createOrEditLabel" data-cy="JobReportCreateUpdateHeading">
            <Translate contentKey="cyberClinicApp.jobReport.home.createOrEditLabel">Create or edit a JobReport</Translate>
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
                  id="job-report-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cyberClinicApp.jobReport.reportDate')}
                id="job-report-reportDate"
                name="reportDate"
                data-cy="reportDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('cyberClinicApp.jobReport.details')}
                id="job-report-details"
                name="details"
                data-cy="details"
                type="textarea"
              />
              <ValidatedField
                label={translate('cyberClinicApp.jobReport.isSuccessful')}
                id="job-report-isSuccessful"
                name="isSuccessful"
                data-cy="isSuccessful"
                check
                type="checkbox"
              />
              <ValidatedField
                id="job-report-jobRequest"
                name="jobRequest"
                data-cy="jobRequest"
                label={translate('cyberClinicApp.jobReport.jobRequest')}
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
                id="job-report-user"
                name="user"
                data-cy="user"
                label={translate('cyberClinicApp.jobReport.user')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/job-report" replace color="info">
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

export default JobReportUpdate;
