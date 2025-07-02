import sys
import json
import warnings
warnings.filterwarnings("ignore")

from youtube_transcript_api import YouTubeTranscriptApi
from youtube_transcript_api._errors import TranscriptsDisabled, NoTranscriptFound

def get_transcript(youtube_video_id):
    try:
        """ If english is not present then go with hindi """
        transcript_list = YouTubeTranscriptApi.get_transcript(youtube_video_id, languages=['en', 'hi'])
        return json.dumps(transcript_list)
    except TranscriptsDisabled:
        return json.dumps({"error": f"Transcripts are disabled for video ID '{youtube_video_id}'."})
    except NoTranscriptFound:
        return json.dumps({"error": f"No transcript available in English or Hindi for video ID '{youtube_video_id}'."})
    except Exception as e:
        return json.dumps({"error": f"Error fetching transcript: {str(e)}"})

if __name__ == "__main__":
    if len(sys.argv) < 2:
        sys.exit(1)

    youtubeVideoID = sys.argv[1]
    transcript = get_transcript(youtubeVideoID)
    print(transcript)