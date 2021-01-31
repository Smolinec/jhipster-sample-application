import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IValues } from 'app/shared/model/values.model';

type EntityResponseType = HttpResponse<IValues>;
type EntityArrayResponseType = HttpResponse<IValues[]>;

@Injectable({ providedIn: 'root' })
export class ValuesService {
  public resourceUrl = SERVER_API_URL + 'api/values';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/values';

  constructor(protected http: HttpClient) {}

  create(values: IValues): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(values);
    return this.http
      .post<IValues>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(values: IValues): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(values);
    return this.http
      .put<IValues>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IValues>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IValues[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IValues[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(values: IValues): IValues {
    const copy: IValues = Object.assign({}, values, {
      timestamp: values.timestamp && values.timestamp.isValid() ? values.timestamp.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.timestamp = res.body.timestamp ? moment(res.body.timestamp) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((values: IValues) => {
        values.timestamp = values.timestamp ? moment(values.timestamp) : undefined;
      });
    }
    return res;
  }
}
