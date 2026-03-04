import { IUser } from 'app/shared/model/user.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import { RequestType } from 'app/shared/model/enumerations/request-type.model';

export interface IJobRequest {
  id?: number;
  fileContentContentType?: string;
  fileContent?: string;
  score?: number | null;
  status?: keyof typeof Status;
  requestType?: keyof typeof RequestType;
  user?: IUser | null;
}

export const defaultValue: Readonly<IJobRequest> = {};
