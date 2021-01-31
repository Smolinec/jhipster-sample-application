import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { PushNotificationTokenUpdateComponent } from 'app/entities/push-notification-token/push-notification-token-update.component';
import { PushNotificationTokenService } from 'app/entities/push-notification-token/push-notification-token.service';
import { PushNotificationToken } from 'app/shared/model/push-notification-token.model';

describe('Component Tests', () => {
  describe('PushNotificationToken Management Update Component', () => {
    let comp: PushNotificationTokenUpdateComponent;
    let fixture: ComponentFixture<PushNotificationTokenUpdateComponent>;
    let service: PushNotificationTokenService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [PushNotificationTokenUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PushNotificationTokenUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PushNotificationTokenUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PushNotificationTokenService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PushNotificationToken(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new PushNotificationToken();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
