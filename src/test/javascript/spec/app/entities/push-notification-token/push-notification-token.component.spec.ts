import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { PushNotificationTokenComponent } from 'app/entities/push-notification-token/push-notification-token.component';
import { PushNotificationTokenService } from 'app/entities/push-notification-token/push-notification-token.service';
import { PushNotificationToken } from 'app/shared/model/push-notification-token.model';

describe('Component Tests', () => {
  describe('PushNotificationToken Management Component', () => {
    let comp: PushNotificationTokenComponent;
    let fixture: ComponentFixture<PushNotificationTokenComponent>;
    let service: PushNotificationTokenService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [PushNotificationTokenComponent],
      })
        .overrideTemplate(PushNotificationTokenComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PushNotificationTokenComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PushNotificationTokenService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new PushNotificationToken(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.pushNotificationTokens && comp.pushNotificationTokens[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
