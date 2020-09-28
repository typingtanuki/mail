# mail
Extract kanjis from emails (mbox format)

## Usage

`MailReader <folder>`

The folder must point to a valid directory containing MBox files (without extension). For instance your thunderbird email directory.

## Output

The tool will output the most frequent groups of kanjis, having a meaning, occuring the email.
The format is:
```
Length <length of the following compounds>:
<occurences>: <word>[<pronunciation>] <meaning>
<occurences>: <word>[<pronunciation>] <meaning>
...
```

Sample:
```
Length 4:
2776: 健康保険[けんこうほけん] health insurance
2034: 株式会社[かぶしきがいしゃ] public company
```

## 3rd party

This tool uses the edict dictonary. Copyright Electronic Dictionary Research & Development Group - 2018
