import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { TemperatureUpdateComponent } from 'app/entities/temperature/temperature-update.component';
import { TemperatureService } from 'app/entities/temperature/temperature.service';
import { Temperature } from 'app/shared/model/temperature.model';

describe('Component Tests', () => {
  describe('Temperature Management Update Component', () => {
    let comp: TemperatureUpdateComponent;
    let fixture: ComponentFixture<TemperatureUpdateComponent>;
    let service: TemperatureService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [TemperatureUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(TemperatureUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TemperatureUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TemperatureService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Temperature(123);
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
        const entity = new Temperature();
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
