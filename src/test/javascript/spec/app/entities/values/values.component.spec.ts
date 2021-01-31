import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { ValuesComponent } from 'app/entities/values/values.component';
import { ValuesService } from 'app/entities/values/values.service';
import { Values } from 'app/shared/model/values.model';

describe('Component Tests', () => {
  describe('Values Management Component', () => {
    let comp: ValuesComponent;
    let fixture: ComponentFixture<ValuesComponent>;
    let service: ValuesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [ValuesComponent],
      })
        .overrideTemplate(ValuesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ValuesComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ValuesService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Values(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.values && comp.values[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
