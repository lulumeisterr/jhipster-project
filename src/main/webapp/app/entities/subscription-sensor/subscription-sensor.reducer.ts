import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISubscriptionSensor, defaultValue } from 'app/shared/model/subscription-sensor.model';

export const ACTION_TYPES = {
  FETCH_SUBSCRIPTIONSENSOR_LIST: 'subscriptionSensor/FETCH_SUBSCRIPTIONSENSOR_LIST',
  FETCH_SUBSCRIPTIONSENSOR: 'subscriptionSensor/FETCH_SUBSCRIPTIONSENSOR',
  CREATE_SUBSCRIPTIONSENSOR: 'subscriptionSensor/CREATE_SUBSCRIPTIONSENSOR',
  UPDATE_SUBSCRIPTIONSENSOR: 'subscriptionSensor/UPDATE_SUBSCRIPTIONSENSOR',
  PARTIAL_UPDATE_SUBSCRIPTIONSENSOR: 'subscriptionSensor/PARTIAL_UPDATE_SUBSCRIPTIONSENSOR',
  DELETE_SUBSCRIPTIONSENSOR: 'subscriptionSensor/DELETE_SUBSCRIPTIONSENSOR',
  RESET: 'subscriptionSensor/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISubscriptionSensor>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type SubscriptionSensorState = Readonly<typeof initialState>;

// Reducer

export default (state: SubscriptionSensorState = initialState, action): SubscriptionSensorState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONSENSOR_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONSENSOR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SUBSCRIPTIONSENSOR):
    case REQUEST(ACTION_TYPES.UPDATE_SUBSCRIPTIONSENSOR):
    case REQUEST(ACTION_TYPES.DELETE_SUBSCRIPTIONSENSOR):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_SUBSCRIPTIONSENSOR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONSENSOR_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONSENSOR):
    case FAILURE(ACTION_TYPES.CREATE_SUBSCRIPTIONSENSOR):
    case FAILURE(ACTION_TYPES.UPDATE_SUBSCRIPTIONSENSOR):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_SUBSCRIPTIONSENSOR):
    case FAILURE(ACTION_TYPES.DELETE_SUBSCRIPTIONSENSOR):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONSENSOR_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONSENSOR):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUBSCRIPTIONSENSOR):
    case SUCCESS(ACTION_TYPES.UPDATE_SUBSCRIPTIONSENSOR):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_SUBSCRIPTIONSENSOR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBSCRIPTIONSENSOR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/subscription-sensors';

// Actions

export const getEntities: ICrudGetAllAction<ISubscriptionSensor> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SUBSCRIPTIONSENSOR_LIST,
  payload: axios.get<ISubscriptionSensor>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<ISubscriptionSensor> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONSENSOR,
    payload: axios.get<ISubscriptionSensor>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ISubscriptionSensor> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUBSCRIPTIONSENSOR,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISubscriptionSensor> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTIONSENSOR,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ISubscriptionSensor> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_SUBSCRIPTIONSENSOR,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISubscriptionSensor> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBSCRIPTIONSENSOR,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
