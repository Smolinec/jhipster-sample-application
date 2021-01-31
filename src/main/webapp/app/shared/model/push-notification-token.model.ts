import { Moment } from 'moment';
import { IWebUser } from 'app/shared/model/web-user.model';

export interface IPushNotificationToken {
  id?: number;
  token?: string;
  timestamp?: Moment;
  webUsers?: IWebUser[];
}

export class PushNotificationToken implements IPushNotificationToken {
  constructor(public id?: number, public token?: string, public timestamp?: Moment, public webUsers?: IWebUser[]) {}
}
