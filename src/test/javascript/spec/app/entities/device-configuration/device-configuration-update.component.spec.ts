import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { DeviceConfigurationUpdateComponent } from 'app/entities/device-configuration/device-configuration-update.component';
import { DeviceConfigurationService } from 'app/entities/device-configuration/device-configuration.service';
import { DeviceConfiguration } from 'app/shared/model/device-configuration.model';

describe('Component Tests', () => {
  describe('DeviceConfiguration Management Update Component', () => {
    let comp: DeviceConfigurationUpdateComponent;
    let fixture: ComponentFixture<DeviceConfigurationUpdateComponent>;
    let service: DeviceConfigurationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [DeviceConfigurationUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(DeviceConfigurationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DeviceConfigurationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DeviceConfigurationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DeviceConfiguration(123);
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
        const entity = new DeviceConfiguration();
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
