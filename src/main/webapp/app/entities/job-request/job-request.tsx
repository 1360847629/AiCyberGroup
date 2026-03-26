import './job-request.scss';

import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, Translate, byteSize, getPaginationState, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './job-request.reducer';

export const JobRequest = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const jobRequestList = useAppSelector(state => state.jobRequest.entities);
  const loading = useAppSelector(state => state.jobRequest.loading);
  const totalItems = useAppSelector(state => state.jobRequest.totalItems);
  const account = useAppSelector(state => state.authentication.account);
  const getAllEntities = () => {
    dispatch(
      getEntities({
        id: account?.id,
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="job-request-heading" data-cy="JobRequestHeading">
        <Translate contentKey="cyberClinicApp.jobRequest.home.title">Job Requests</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="cyberClinicApp.jobRequest.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/job-request/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="cyberClinicApp.jobRequest.home.createLabel">Create new Job Request</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {jobRequestList && jobRequestList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="cyberClinicApp.jobRequest.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('fileContent')}>
                  <Translate contentKey="cyberClinicApp.jobRequest.fileContent">File Content</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fileContent')} />
                </th>
                <th className="hand" onClick={sort('score')}>
                  <Translate contentKey="cyberClinicApp.jobRequest.score">Score</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('score')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="cyberClinicApp.jobRequest.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('fileType')}>
                  <Translate contentKey="cyberClinicApp.jobRequest.fileType">File Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fileType')} />
                </th>
                <th className="hand" onClick={sort('requestType')}>
                  <Translate contentKey="cyberClinicApp.jobRequest.requestType">Request Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('requestType')} />
                </th>
                <th className="hand" onClick={sort('priority')}>
                  <Translate contentKey="cyberClinicApp.jobRequest.priority">Priority</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('priority')} />
                </th>
                <th className="hand" onClick={sort('fileName')}>
                  <Translate contentKey="cyberClinicApp.jobRequest.fileName">File Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fileName')} />
                </th>
                <th>
                  <Translate contentKey="cyberClinicApp.jobRequest.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {jobRequestList.map((jobRequest, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/job-request/${jobRequest.id}`} color="link" size="sm">
                      {jobRequest.id}
                    </Button>
                  </td>
                  <td>
                    {jobRequest.fileContent ? (
                      <div>
                        {jobRequest.fileContentContentType ? (
                          <a onClick={openFile(jobRequest.fileContentContentType, jobRequest.fileContent)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {jobRequest.fileContentContentType}, {byteSize(jobRequest.fileContent)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{jobRequest.score}</td>
                  <td>
                    <Translate contentKey={`cyberClinicApp.Status.${jobRequest.status}`} />
                  </td>
                  <td>
                    <Translate contentKey={`cyberClinicApp.FileType.${jobRequest.fileType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`cyberClinicApp.RequestType.${jobRequest.requestType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`cyberClinicApp.Priority.${jobRequest.priority}`} />
                  </td>
                  <td>{jobRequest.fileName}</td>
                  <td>{jobRequest.user ? jobRequest.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        onClick={() =>
                          (window.location.href = `/job-request/${jobRequest.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="cyberClinicApp.jobRequest.home.notFound">No Job Requests found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={jobRequestList && jobRequestList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default JobRequest;
