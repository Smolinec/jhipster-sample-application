import { IPushNotificationToken } from 'app/shared/model/push-notification-token.model';
import { IPlace } from 'app/shared/model/place.model';

export interface IWebUser {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  password?: string;
  pushNotificationTokens?: IPushNotificationToken[];
  places?: IPlace[];
}

export class WebUser implements IWebUser {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public password?: string,
    public pushNotificationTokens?: IPushNotificationToken[],
    public places?: IPlace[]
  ) {}
}
