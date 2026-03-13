import { IUser } from 'app/shared/model/user.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import { FileType } from 'app/shared/model/enumerations/file-type.model';
import { RequestType } from 'app/shared/model/enumerations/request-type.model';
import { Priority } from 'app/shared/model/enumerations/priority.model';

export interface IJobRequest {
  id?: number;
  fileContentContentType?: string;
  fileContent?: string;
  score?: number | null;
  status?: keyof typeof Status;
  fileType?: keyof typeof FileType;
  requestType?: keyof typeof RequestType;
  priority?: keyof typeof Priority;
  fileName?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IJobRequest> = {};
