export interface ISubscriptionSensor {
  id?: number;
  telegramId?: number | null;
  name?: string | null;
}

export const defaultValue: Readonly<ISubscriptionSensor> = {};
