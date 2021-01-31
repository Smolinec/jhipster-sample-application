import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { ValuesUpdateComponent } from 'app/entities/values/values-update.component';
import { ValuesService } from 'app/entities/values/values.service';
import { Values } from 'app/shared/model/values.model';

describe('Component Tests', () => {
  describe('Values Management Update Component', () => {
    let comp: ValuesUpdateComponent;
    let fixture: ComponentFixture<ValuesUpdateComponent>;
    let service: ValuesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [ValuesUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ValuesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ValuesUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ValuesService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Values(123);
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
        const entity = new Values();
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
