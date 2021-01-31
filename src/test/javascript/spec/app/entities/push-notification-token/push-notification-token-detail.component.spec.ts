import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { PushNotificationTokenDetailComponent } from 'app/entities/push-notification-token/push-notification-token-detail.component';
import { PushNotificationToken } from 'app/shared/model/push-notification-token.model';

describe('Component Tests', () => {
  describe('PushNotificationToken Management Detail Component', () => {
    let comp: PushNotificationTokenDetailComponent;
    let fixture: ComponentFixture<PushNotificationTokenDetailComponent>;
    const route = ({ data: of({ pushNotificationToken: new PushNotificationToken(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [PushNotificationTokenDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PushNotificationTokenDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PushNotificationTokenDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pushNotificationToken on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pushNotificationToken).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
