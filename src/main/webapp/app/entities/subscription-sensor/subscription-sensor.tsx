import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './subscription-sensor.reducer';
import { ISubscriptionSensor } from 'app/shared/model/subscription-sensor.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionSensorProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const SubscriptionSensor = (props: ISubscriptionSensorProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const handleSyncList = () => {
    props.getEntities();
  };

  const { subscriptionSensorList, match, loading } = props;
  return (
    <div>
      <h2 id="subscription-sensor-heading" data-cy="SubscriptionSensorHeading">
        <Translate contentKey="sensorsApp.subscriptionSensor.home.title">Subscription Sensors</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sensorsApp.subscriptionSensor.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sensorsApp.subscriptionSensor.home.createLabel">Create new Subscription Sensor</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {subscriptionSensorList && subscriptionSensorList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="sensorsApp.subscriptionSensor.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="sensorsApp.subscriptionSensor.telegramId">Telegram Id</Translate>
                </th>
                <th>
                  <Translate contentKey="sensorsApp.subscriptionSensor.name">Name</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {subscriptionSensorList.map((subscriptionSensor, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${subscriptionSensor.id}`} color="link" size="sm">
                      {subscriptionSensor.id}
                    </Button>
                  </td>
                  <td>{subscriptionSensor.telegramId}</td>
                  <td>{subscriptionSensor.name}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${subscriptionSensor.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${subscriptionSensor.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${subscriptionSensor.id}/delete`}
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
              <Translate contentKey="sensorsApp.subscriptionSensor.home.notFound">No Subscription Sensors found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ subscriptionSensor }: IRootState) => ({
  subscriptionSensorList: subscriptionSensor.entities,
  loading: subscriptionSensor.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionSensor);
