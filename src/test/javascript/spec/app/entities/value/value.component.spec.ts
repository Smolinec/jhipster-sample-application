import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { ValueComponent } from 'app/entities/value/value.component';
import { ValueService } from 'app/entities/value/value.service';
import { Value } from 'app/shared/model/value.model';

describe('Component Tests', () => {
  describe('Value Management Component', () => {
    let comp: ValueComponent;
    let fixture: ComponentFixture<ValueComponent>;
    let service: ValueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [ValueComponent],
      })
        .overrideTemplate(ValueComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ValueComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ValueService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Value(123)],
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
