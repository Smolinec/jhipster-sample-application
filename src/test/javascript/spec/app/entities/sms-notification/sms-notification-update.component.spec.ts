import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { SMSNotificationUpdateComponent } from 'app/entities/sms-notification/sms-notification-update.component';
import { SMSNotificationService } from 'app/entities/sms-notification/sms-notification.service';
import { SMSNotification } from 'app/shared/model/sms-notification.model';

describe('Component Tests', () => {
  describe('SMSNotification Management Update Component', () => {
    let comp: SMSNotificationUpdateComponent;
    let fixture: ComponentFixture<SMSNotificationUpdateComponent>;
    let service: SMSNotificationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [SMSNotificationUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SMSNotificationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SMSNotificationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SMSNotificationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SMSNotification(123);
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
        const entity = new SMSNotification();
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
