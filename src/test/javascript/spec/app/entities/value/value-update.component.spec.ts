import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { ValueUpdateComponent } from 'app/entities/value/value-update.component';
import { ValueService } from 'app/entities/value/value.service';
import { Value } from 'app/shared/model/value.model';

describe('Component Tests', () => {
  describe('Value Management Update Component', () => {
    let comp: ValueUpdateComponent;
    let fixture: ComponentFixture<ValueUpdateComponent>;
    let service: ValueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [ValueUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ValueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ValueUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ValueService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Value(123);
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
        const entity = new Value();
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
