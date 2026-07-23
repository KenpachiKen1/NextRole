export interface ResumeResponse{
    id: number,
    resumeTitle: string,
    fileSize: number,
    uploadedAt: string
}

export interface ViewSingleResumeResponse{
    id: number,
    resumeTitle: string,
    fileSize: number,
    uploadedAt: string,
    url: string
}


/*
Gotta rework this
*/
export interface UpdateResumeRequest {
    resumeTitle: string
}

export interface CreateResumeRequest{
    resumeTitle: string,
    file: File
}



