import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { DeviceProfileComponent } from 'app/entities/device-profile/device-profile.component';
import { DeviceProfileService } from 'app/entities/device-profile/device-profile.service';
import { DeviceProfile } from 'app/shared/model/device-profile.model';

describe('Component Tests', () => {
  describe('DeviceProfile Management Component', () => {
    let comp: DeviceProfileComponent;
    let fixture: ComponentFixture<DeviceProfileComponent>;
    let service: DeviceProfileService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [DeviceProfileComponent],
      })
        .overrideTemplate(DeviceProfileComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DeviceProfileComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DeviceProfileService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new DeviceProfile(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.deviceProfiles && comp.deviceProfiles[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
