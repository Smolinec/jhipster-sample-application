import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { DeviceProfileUpdateComponent } from 'app/entities/device-profile/device-profile-update.component';
import { DeviceProfileService } from 'app/entities/device-profile/device-profile.service';
import { DeviceProfile } from 'app/shared/model/device-profile.model';

describe('Component Tests', () => {
  describe('DeviceProfile Management Update Component', () => {
    let comp: DeviceProfileUpdateComponent;
    let fixture: ComponentFixture<DeviceProfileUpdateComponent>;
    let service: DeviceProfileService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [DeviceProfileUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(DeviceProfileUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DeviceProfileUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DeviceProfileService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DeviceProfile(123);
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
        const entity = new DeviceProfile();
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
