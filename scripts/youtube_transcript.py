import sys
import json
from youtube_transcript_api import YouTubeTranscriptApi

def get_transcript(youtubeVideoID):
    try:
        """ If english is not present then go with hindi """
        transcript_list = YouTubeTranscriptApi.get_transcript(youtubeVideoID, languages=['en', 'hi'])

        # Format transcript as plain text
        transcript_text = ""
        """for entry in transcript_list:
            transcript_text += f"{entry['text']} """

        for entry in transcript_list:
            start = entry['start']
            duration = entry['duration']
            text = entry['text']
            end = start + duration
            transcript_text += f"[{start:.2f}s - {end:.2f}s] {text}\n"

        return transcript_text.strip()
    except Exception as e:
        return f"Error fetching transcript: {str(e)}"

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Please provide a YouTube URL as an argument")
        sys.exit(1)

    youtubeVideoID = sys.argv[1]
    transcript = get_transcript(youtubeVideoID)
    print(transcript)