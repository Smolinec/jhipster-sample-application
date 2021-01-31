import { IDevice } from 'app/shared/model/device.model';

export interface IDeviceProfile {
  id?: number;
  devices?: IDevice[];
}

export class DeviceProfile implements IDeviceProfile {
  constructor(public id?: number, public devices?: IDevice[]) {}
}
