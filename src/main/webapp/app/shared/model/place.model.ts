import { IWebUser } from 'app/shared/model/web-user.model';

export interface IPlace {
  id?: number;
  name?: string;
  webUsers?: IWebUser[];
}

export class Place implements IPlace {
  constructor(public id?: number, public name?: string, public webUsers?: IWebUser[]) {}
}
