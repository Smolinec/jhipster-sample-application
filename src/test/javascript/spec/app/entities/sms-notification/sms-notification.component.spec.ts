import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { SMSNotificationComponent } from 'app/entities/sms-notification/sms-notification.component';
import { SMSNotificationService } from 'app/entities/sms-notification/sms-notification.service';
import { SMSNotification } from 'app/shared/model/sms-notification.model';

describe('Component Tests', () => {
  describe('SMSNotification Management Component', () => {
    let comp: SMSNotificationComponent;
    let fixture: ComponentFixture<SMSNotificationComponent>;
    let service: SMSNotificationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [SMSNotificationComponent],
      })
        .overrideTemplate(SMSNotificationComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SMSNotificationComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SMSNotificationService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new SMSNotification(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.sMSNotifications && comp.sMSNotifications[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
