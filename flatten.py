#! /usr/local/bin/python

import argparse
import os
import shutil
import sys
import tempfile

import git

IGNORE_PATTERNS = ('.git', ".DS_Store")
SAFE_CHARS = ["-", "_", "."]
MAX_LENGTH = 100

STUDENT = "student"
DEVELOP = "develop"


def flatten():
    repo = git.Repo(os.getcwd())

    remove_local_branches(repo, STUDENT, DEVELOP)
    repo.git.clean("-fdx")
    try:
        temp_dir = tempfile.mkdtemp()
        to_temp_dir(repo, os.getcwd(), DEVELOP, temp_dir)
        copy_snapshots(repo, STUDENT, temp_dir, os.getcwd())
    finally:
        if os.path.exists(temp_dir):
            shutil.rmtree(temp_dir)

    print "Done! Review and commit the", STUDENT, "branch at your leisure."
    print "Then run $ git push --all --prune"


def remove_local_branches(repo, student, develop):
    for branch in repo.branches:
        if branch.name != student and branch.name != develop:
            print "Removing local branch:", branch.name
            repo.git.branch(branch.name, "-D")


def to_temp_dir(repo, repo_dir, develop, temp_dir):
    for rev in repo.git.rev_list(develop).split("\n"):
        commit = repo.commit(rev)
        branch_name = clean_commit_message(commit.message)
        if "Exercise" in branch_name or "Solution" in branch_name:
            if branch_name in repo.branches:
                repo.git.branch(branch_name, "-D")
            new_branch = repo.create_head(branch_name)
            new_branch.set_commit(rev)

            repo.git.checkout(commit)
            print "Saving snapshot of:", branch_name
            repo.git.clean("-fdx")
            target_dir = os.path.join(temp_dir, branch_name)

            shutil.copytree(repo_dir, target_dir,
                            ignore=shutil.ignore_patterns(*IGNORE_PATTERNS))


def clean_commit_message(message):
    first_line = message.split("\n")[0]
    safe_message = "".join(
        c for c in first_line if c.isalnum() or c in SAFE_CHARS).strip()
    return safe_message[:MAX_LENGTH] if len(safe_message) > MAX_LENGTH else safe_message


def copy_snapshots(repo, student, temp_dir, target_dir):
    repo.git.checkout(student)
    for item in os.listdir(temp_dir):
        source_dir = os.path.join(temp_dir, item)
        dest_dir = os.path.join(target_dir, item)

        if os.path.exists(dest_dir):
            shutil.rmtree(dest_dir)
        print "Copying: ", item
        shutil.copytree(source_dir, dest_dir)




def main():
    flatten()


if __name__ == "__main__":
    sys.exit(main())
