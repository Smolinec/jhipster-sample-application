import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { ITemperature } from 'app/shared/model/temperature.model';

type EntityResponseType = HttpResponse<ITemperature>;
type EntityArrayResponseType = HttpResponse<ITemperature[]>;

@Injectable({ providedIn: 'root' })
export class TemperatureService {
  public resourceUrl = SERVER_API_URL + 'api/temperatures';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/temperatures';

  constructor(protected http: HttpClient) {}

  create(temperature: ITemperature): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(temperature);
    return this.http
      .post<ITemperature>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(temperature: ITemperature): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(temperature);
    return this.http
      .put<ITemperature>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITemperature>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITemperature[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITemperature[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(temperature: ITemperature): ITemperature {
    const copy: ITemperature = Object.assign({}, temperature, {
      createTimestamp:
        temperature.createTimestamp && temperature.createTimestamp.isValid() ? temperature.createTimestamp.toJSON() : undefined,
      lastUpdateTimestamp:
        temperature.lastUpdateTimestamp && temperature.lastUpdateTimestamp.isValid() ? temperature.lastUpdateTimestamp.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createTimestamp = res.body.createTimestamp ? moment(res.body.createTimestamp) : undefined;
      res.body.lastUpdateTimestamp = res.body.lastUpdateTimestamp ? moment(res.body.lastUpdateTimestamp) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((temperature: ITemperature) => {
        temperature.createTimestamp = temperature.createTimestamp ? moment(temperature.createTimestamp) : undefined;
        temperature.lastUpdateTimestamp = temperature.lastUpdateTimestamp ? moment(temperature.lastUpdateTimestamp) : undefined;
      });
    }
    return res;
  }
}
