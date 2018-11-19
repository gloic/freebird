import {Component, EventEmitter, Input, Output} from "@angular/core";
import {AdminTabStatsModel} from "./admin-tab-stats.model";

@Component({
  moduleId: module.id,
  selector: 'freebird-admin-tab-stats',
  templateUrl: 'admin-tab-stats.component.html'
})
export class AdminTabStatsComponent {

  @Input()
  model: AdminTabStatsModel;

  @Output()
  onScanAllClickedEvent = new EventEmitter<boolean>();

  @Output()
  onRefreshClickedEvent = new EventEmitter<boolean>();

  @Output()
  onExportSitesClickedEvent = new EventEmitter<boolean>();

  @Output()
  onImportSitesEvent = new EventEmitter<boolean>();

  file: FileList;

  onScanAllClicked(checked: boolean) {
    this.onScanAllClickedEvent.emit(checked);
  }

  onRefreshClicked() {
    this.onRefreshClickedEvent.emit();
  }

  onExportSitesClicked() {
    this.onExportSitesClickedEvent.emit();
  }

  fileChange(event) {
    var reader:FileReader = new FileReader();
    reader.onloadend = (e) => {
      this.onImportSitesEvent.emit(reader.result.split("\r\n"));
      console.log(this.file);
      this.file = null;
    };
    reader.readAsText(event.target.files[0], 'UTF-8');
  }
}
