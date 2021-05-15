import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './subscription-sensor.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionSensorDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SubscriptionSensorDetail = (props: ISubscriptionSensorDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { subscriptionSensorEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="subscriptionSensorDetailsHeading">
          <Translate contentKey="jayjayApplicationApp.subscriptionSensor.detail.title">SubscriptionSensor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{subscriptionSensorEntity.id}</dd>
          <dt>
            <span id="telegramId">
              <Translate contentKey="jayjayApplicationApp.subscriptionSensor.telegramId">Telegram Id</Translate>
            </span>
          </dt>
          <dd>{subscriptionSensorEntity.telegramId}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="jayjayApplicationApp.subscriptionSensor.name">Name</Translate>
            </span>
          </dt>
          <dd>{subscriptionSensorEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/subscription-sensor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscription-sensor/${subscriptionSensorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ subscriptionSensor }: IRootState) => ({
  subscriptionSensorEntity: subscriptionSensor.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SubscriptionSensorDetail);
