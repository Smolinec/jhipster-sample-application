import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { WebUserUpdateComponent } from 'app/entities/web-user/web-user-update.component';
import { WebUserService } from 'app/entities/web-user/web-user.service';
import { WebUser } from 'app/shared/model/web-user.model';

describe('Component Tests', () => {
  describe('WebUser Management Update Component', () => {
    let comp: WebUserUpdateComponent;
    let fixture: ComponentFixture<WebUserUpdateComponent>;
    let service: WebUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [WebUserUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(WebUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WebUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WebUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new WebUser(123);
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
        const entity = new WebUser();
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
