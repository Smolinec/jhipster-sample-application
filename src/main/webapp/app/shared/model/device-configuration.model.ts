export interface IDeviceConfiguration {
  id?: number;
  primaryHostname?: string;
  secondaryHostname?: string;
  port?: number;
}

export class DeviceConfiguration implements IDeviceConfiguration {
  constructor(public id?: number, public primaryHostname?: string, public secondaryHostname?: string, public port?: number) {}
}
