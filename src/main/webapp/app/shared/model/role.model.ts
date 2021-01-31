import { IWebUser } from 'app/shared/model/web-user.model';

export interface IRole {
  id?: number;
  name?: string;
  webUsers?: IWebUser[];
}

export class Role implements IRole {
  constructor(public id?: number, public name?: string, public webUsers?: IWebUser[]) {}
}
