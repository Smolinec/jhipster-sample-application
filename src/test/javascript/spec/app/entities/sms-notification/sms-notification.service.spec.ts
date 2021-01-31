import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { SMSNotificationService } from 'app/entities/sms-notification/sms-notification.service';
import { ISMSNotification, SMSNotification } from 'app/shared/model/sms-notification.model';
import { AlertType } from 'app/shared/model/enumerations/alert-type.model';

describe('Service Tests', () => {
  describe('SMSNotification Service', () => {
    let injector: TestBed;
    let service: SMSNotificationService;
    let httpMock: HttpTestingController;
    let elemDefault: ISMSNotification;
    let expectedResult: ISMSNotification | ISMSNotification[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(SMSNotificationService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new SMSNotification(
        0,
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        false,
        currentDate,
        false,
        currentDate,
        AlertType.INFO,
        currentDate
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdTimestamp: currentDate.format(DATE_TIME_FORMAT),
            sendingTimestamp: currentDate.format(DATE_TIME_FORMAT),
            sendTimestamp: currentDate.format(DATE_TIME_FORMAT),
            featureSend: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a SMSNotification', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdTimestamp: currentDate.format(DATE_TIME_FORMAT),
            sendingTimestamp: currentDate.format(DATE_TIME_FORMAT),
            sendTimestamp: currentDate.format(DATE_TIME_FORMAT),
            featureSend: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdTimestamp: currentDate,
            sendingTimestamp: currentDate,
            sendTimestamp: currentDate,
            featureSend: currentDate,
          },
          returnedFromService
        );

        service.create(new SMSNotification()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SMSNotification', () => {
        const returnedFromService = Object.assign(
          {
            telNumber: 'BBBBBB',
            message: 'BBBBBB',
            createdTimestamp: currentDate.format(DATE_TIME_FORMAT),
            uuidDevice: 'BBBBBB',
            isSending: true,
            sendingTimestamp: currentDate.format(DATE_TIME_FORMAT),
            isSend: true,
            sendTimestamp: currentDate.format(DATE_TIME_FORMAT),
            alertType: 'BBBBBB',
            featureSend: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdTimestamp: currentDate,
            sendingTimestamp: currentDate,
            sendTimestamp: currentDate,
            featureSend: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SMSNotification', () => {
        const returnedFromService = Object.assign(
          {
            telNumber: 'BBBBBB',
            message: 'BBBBBB',
            createdTimestamp: currentDate.format(DATE_TIME_FORMAT),
            uuidDevice: 'BBBBBB',
            isSending: true,
            sendingTimestamp: currentDate.format(DATE_TIME_FORMAT),
            isSend: true,
            sendTimestamp: currentDate.format(DATE_TIME_FORMAT),
            alertType: 'BBBBBB',
            featureSend: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdTimestamp: currentDate,
            sendingTimestamp: currentDate,
            sendTimestamp: currentDate,
            featureSend: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a SMSNotification', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
