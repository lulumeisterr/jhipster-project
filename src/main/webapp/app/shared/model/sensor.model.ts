export interface ISensor {
  id?: number;
  variable?: string | null;
  unit?: string | null;
  value?: string | null;
}

export const defaultValue: Readonly<ISensor> = {};
