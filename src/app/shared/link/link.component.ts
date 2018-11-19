import {Component, Input, Output, EventEmitter} from "@angular/core";
import {LinkDetail} from "./link-detail";
import {ConfirmationService} from "primeng/components/common/api";
@Component({
  moduleId: module.id,
  selector: 'media-links',
  templateUrl: 'link.component.html'
})
export class LinkComponent {

  @Input()
  links: LinkDetail[];

  @Output()
  onBadIdentificationEvent = new EventEmitter<LinkDetail>();

  constructor(private confirmationService: ConfirmationService) {}

  confirm(link: LinkDetail) {
    console.log('Report a wrong identification');
    this.confirmationService.confirm({
      message: 'Do you want to assign this media to the "Unknown" category?',
      accept: () => {
        this.onBadIdentificationEvent.emit(link);
      }
    });
  }

}
