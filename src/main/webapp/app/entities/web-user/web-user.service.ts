import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IWebUser } from 'app/shared/model/web-user.model';

type EntityResponseType = HttpResponse<IWebUser>;
type EntityArrayResponseType = HttpResponse<IWebUser[]>;

@Injectable({ providedIn: 'root' })
export class WebUserService {
  public resourceUrl = SERVER_API_URL + 'api/web-users';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/web-users';

  constructor(protected http: HttpClient) {}

  create(webUser: IWebUser): Observable<EntityResponseType> {
    return this.http.post<IWebUser>(this.resourceUrl, webUser, { observe: 'response' });
  }

  update(webUser: IWebUser): Observable<EntityResponseType> {
    return this.http.put<IWebUser>(this.resourceUrl, webUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWebUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWebUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWebUser[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
